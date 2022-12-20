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
    public class FuncionarioLogic
    {
        public static async Task<Response> GetFuncionariosLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Funcionario> funcionarioList = await FuncionarioService.GetAllService(sqlDataSource);

            if (funcionarioList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de funcionarios obtida com sucesso";
                response.Data = new JsonResult(funcionarioList);
            }

            return response;
        }

        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            Funcionario funcionario = await FuncionarioService.GetByIDService(sqlDataSource, targetID);

            if (funcionario != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Funcionario obtido com sucesso";
                response.Data = new JsonResult(funcionario);
            }

            return response;
        }

        public static async Task<Response> PostLogic(string sqlDataSource, Funcionario newFuncionario)
        {
            Response response = new Response();
            bool creationResult = await FuncionarioService.PostService(sqlDataSource, newFuncionario);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Funcionario adicionado com sucesso");
            }

            return response;
        }

        public static async Task<Response> PatchLogic(string sqlDataSource, Funcionario funcionario, int targetID)
        {
            Response response = new Response();
            bool updateResult = await FuncionarioService.PatchService(sqlDataSource, funcionario, targetID);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Funcionario alterado com sucesso");
            }

            return response;
        }

        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await FuncionarioService.DeleteService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Funcionario removido com sucesso");
            }

            return response;
        }
    }
}
