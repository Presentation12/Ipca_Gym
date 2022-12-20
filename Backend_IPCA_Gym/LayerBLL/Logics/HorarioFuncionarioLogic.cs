using LayerBLL.Utils;
using LayerBOL.Models;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;
using LayerDAL.Services;

namespace LayerBLL.Logics
{
    public class HorarioFuncionarioLogic
    {
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<HorarioFuncionario> horarioFuncionarioList = await HorarioFuncionarioService.GetAllService(sqlDataSource);
            
            if (horarioFuncionarioList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult(horarioFuncionarioList);
            }
            
            return response;
        }

        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            HorarioFuncionario horario = await HorarioFuncionarioService.GetByIDService(sqlDataSource, targetID);

            if (horario != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Horario obtido com sucesso!";
                response.Data = new JsonResult(horario);
            }

            return response;
        }

        public static async Task<Response> PostLogic(string sqlDataSource, HorarioFuncionario newHorario)
        {
            Response response = new Response();
            bool creationResult = await HorarioFuncionarioService.PostService(sqlDataSource, newHorario);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Horario criado com sucesso!");
            }

            return response;
        }

        public static async Task<Response> PatchLogic(string sqlDataSource, HorarioFuncionario horario, int targetID)
        {
            Response response = new Response();
            bool updateResult = await HorarioFuncionarioService.PatchService(sqlDataSource, horario, targetID);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Horario alterado com sucesso!");
            }

            return response;
        }

        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await HorarioFuncionarioService.DeleteService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Horario apagado com sucesso!");
            }

            return response;
        }
    }
}
